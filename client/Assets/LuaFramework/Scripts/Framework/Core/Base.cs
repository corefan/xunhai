using UnityEngine;
using System.Collections;
using LuaFramework;
using System.Collections.Generic;

public class Base : MonoBehaviour {
    private AppFacade m_Facade;
    private LuaManager m_LuaMgr;
    private LoaderManager m_loadMgr;
    private ResourceManager m_ResMgr;
    private SoundManager m_SoundMgr;
    private TimerManager m_TimerMgr;
    //private ThreadManager m_ThreadMgr;
    private ObjectPoolManager m_ObjectPoolMgr;

    protected AppFacade facade {
        get {
            if (m_Facade == null) {
                m_Facade = AppFacade.Instance;
            }
            return m_Facade;
        }
    }

    protected LuaManager LuaManager {
        get {
            if (m_LuaMgr == null) {
                m_LuaMgr = facade.GetManager<LuaManager>(ManagerName.Lua);
            }
            return m_LuaMgr;
        }
    }

    protected LoaderManager loaderMgr
    {
        get
        {
            if (m_loadMgr == null)
            {
                m_loadMgr = facade.GetManager<LoaderManager>(ManagerName.Loader);
            }
            return m_loadMgr;
        }
    }

    protected ResourceManager ResManager {
        get {
            if (m_ResMgr == null) {
                m_ResMgr = facade.GetManager<ResourceManager>(ManagerName.Resource);
            }
            return m_ResMgr;
        }
    }


    protected SoundManager SoundManager {
        get {
            if (m_SoundMgr == null) {
                m_SoundMgr = facade.GetManager<SoundManager>(ManagerName.Sound);
            }
            return m_SoundMgr;
        }
    }

    protected TimerManager TimerManager {
        get {
            if (m_TimerMgr == null) {
                m_TimerMgr = facade.GetManager<TimerManager>(ManagerName.Timer);
            }
            return m_TimerMgr;
        }
    }

    protected ObjectPoolManager ObjPoolManager {
        get {
            if (m_ObjectPoolMgr == null) {
                m_ObjectPoolMgr = facade.GetManager<ObjectPoolManager>(ManagerName.ObjectPool);
            }
            return m_ObjectPoolMgr;
        }
    }
}
