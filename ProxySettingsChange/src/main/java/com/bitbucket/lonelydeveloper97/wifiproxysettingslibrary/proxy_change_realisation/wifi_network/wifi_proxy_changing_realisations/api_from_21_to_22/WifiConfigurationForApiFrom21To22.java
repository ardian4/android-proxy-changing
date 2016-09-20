package com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.wifi_proxy_changing_realisations.api_from_21_to_22;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ProxyInfo;
import android.os.Build;

import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.exceptions.NullWifiConfigurationException;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.exceptions.SdkNotSupportedException;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.exceptions.WifiProxyInfoNotSettedException;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.reflection_realisation.ReflectionHelper;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.wifi_proxy_changing_realisations.BaseWifiConfiguration;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.wifi_proxy_changing_realisations.ProxyChanger;
import com.bitbucket.lonelydeveloper97.wifiproxysettingslibrary.proxy_change_realisation.wifi_network.wifi_proxy_changing_realisations.ProxySettings;

import java.lang.reflect.InvocationTargetException;


public class WifiConfigurationForApiFrom21To22 extends BaseWifiConfiguration implements ProxyChanger {

    public WifiConfigurationForApiFrom21To22(Context context) throws NullWifiConfigurationException {
        super(context);
    }

    public static WifiConfigurationForApiFrom21To22 createFromCurrentContext(Context context) throws NullWifiConfigurationException {
        return new WifiConfigurationForApiFrom21To22(context);
    }

    @Override
    public void setProxySettings(ProxySettings proxySettings)
            throws NoSuchFieldException, IllegalAccessException {
        ReflectionHelper.setEnumField(getIpConfigurationObject(), proxySettings.getValue(), "proxySettings");
    }

    @Override
    public void setProxyHostAndPort(String host, int port)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        setProxyInfo(ProxyInfoConstructor.proxyInfo(host, port));
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public String getProxyHost()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SdkNotSupportedException {
        ProxyInfo info = getProxyInfo();
        if (info == null)
            throw new WifiProxyInfoNotSettedException();
        return info.getHost();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public int getProxyPort()
            throws SdkNotSupportedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ProxyInfo info = getProxyInfo();
        if (info == null)
            throw new WifiProxyInfoNotSettedException();
        return info.getPort();
    }

    @Override
    public boolean isProxySetted()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            SdkNotSupportedException, NoSuchFieldException {
        return getProxyInfo() == null;
    }

    @Override
    public ProxySettings getProxySettings() throws NoSuchFieldException, IllegalAccessException {
        return ProxySettings.valueOf(String.valueOf( ReflectionHelper.getDeclaredField(getIpConfigurationObject(),"proxySettings")));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setIpAssignment(IpAssignment ipAssignment)
            throws NoSuchFieldException, IllegalAccessException, SdkNotSupportedException {
        ReflectionHelper.setEnumField(getIpConfigurationObject(), ipAssignment.value, "ipAssignment");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IpAssignment getIpAssignment() throws NoSuchFieldException, IllegalAccessException {
        return IpAssignment.valueOf(String.valueOf(ReflectionHelper.getDeclaredField(getIpConfigurationObject(),"ipAssignment")));
    }

    private ProxyInfo getProxyInfo()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (ProxyInfo) ReflectionHelper.getMethodAndInvokeIt(wifiConfiguration,"getHttpProxy");
    }

    private void setProxyInfo(ProxyInfo proxyInfo)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        ReflectionHelper.setDeclaredField(getIpConfigurationObject(), "httpProxy", proxyInfo);
    }

    private Object getIpConfigurationObject()
            throws NoSuchFieldException, IllegalAccessException {
        return ReflectionHelper.getDeclaredField(wifiConfiguration, "mIpConfiguration");
    }


}