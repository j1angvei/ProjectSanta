package cn.j1angvei.adbfx;

import cn.j1angvei.adbfx.home.HomeModel;
import com.android.ddmlib.IDevice;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController<M> implements Initializable {
    private ObjectProperty<IDevice> mChosenDevice;
    private M mModel;
    private ResourceBundle resourceBundle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resourceBundle = resources;
        mChosenDevice = HomeModel.getInstance().getSelectedDevice();
        mModel = initModel();
        initArguments();
        initView();
        initData();
    }

    protected abstract M initModel();

    protected abstract void initArguments();


    protected abstract void initView();

    protected abstract void initData();


    public final ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    public final M getModel() {
        return mModel;
    }

    public IDevice getChosenDevice() {
        return mChosenDevice.get();
    }

    public ObjectProperty<IDevice> chosenDeviceProperty() {
        return mChosenDevice;
    }
}
