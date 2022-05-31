package org.func.spring.boot.setting;

/**
 * @author Yiur
 */
public class MethodSettingFactory implements SettingFactory {

    private MethodSetting methodSetting;

    @Override
    public Setting create() {
        this.methodSetting = new MethodSetting();
        return methodSetting;
    }

    public MethodSetting getSetting() {
        return methodSetting;
    }

}
