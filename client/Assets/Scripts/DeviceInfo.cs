using UnityEngine;
using System.Collections;

public class DeviceInfo{
    public static DeviceInfo _instance = null;
    public static DeviceInfo Instance
    {
        get
        {
            if (_instance == null)
            {
                _instance = new DeviceInfo();
            }
            return _instance;

        }
    }

    public string GetDeviceUID()
    {
        return SystemInfo.deviceUniqueIdentifier;
    }
}
