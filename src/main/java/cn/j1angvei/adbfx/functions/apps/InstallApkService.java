package cn.j1angvei.adbfx.functions.apps;

import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.InstallReceiver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

/**
 * @author j1angvei
 * @since 18/7/14
 */
@Slf4j
public class InstallApkService extends Service<String> {

    private InstallApkModel mInstallApkModel;

    public InstallApkService(InstallApkModel installApkModel) {
        mInstallApkModel = installApkModel;
    }

    @Override
    protected Task<String> createTask() {
        return new InstallApkTask();
    }

    private class InstallApkTask extends Task<String> {
        @Override
        protected String call() throws Exception {
            IDevice device = mInstallApkModel.getChosenDevice().get();
            if (device == null) {
                log.error("No device is chosen to install apk", new NullPointerException("device is null"));
                return null;
            }

            List<String> apks = mInstallApkModel.getApksToInstall();
            int count = apks.size();

            Set<String> args = mInstallApkModel.getInstallArgs();
            String[] argsArray = args.toArray(new String[0]);

            InstallReceiver receiver;
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < apks.size(); i++) {
                updateProgress(i, count);

                String path = apks.get(i);

                receiver = new InstallReceiver();
                try {
                    device.installPackage(path, false, receiver, argsArray);
                } catch (InstallException e) {
                    log.error("Error when install apk,{}", path, e);
                }

                builder.append(i).append(".\t").append(path).append(":\n");
                if (receiver.isSuccessfullyCompleted()) {
                    builder.append("Success");
                } else {
                    builder.append(String.format("Failure - [%s]", receiver.getErrorMessage()));
                }
                builder.append("\n");
            }
            updateProgress(count, count);
            Thread.sleep(1000);
            return builder.toString();
        }
    }
}
