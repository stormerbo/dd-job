import {CalendarTwoTone, DashboardTwoTone, DatabaseTwoTone, SettingTwoTone, SmileTwoTone} from "@ant-design/icons";
import Dashboard from "./pages/dashboard/dashboard";
import User from "./pages/user/user";
import Executor from "./pages/executor/executor";
import Task from "./pages/task/task";
import Log from "./pages/log/log";


export default {
    routes: [
        {
            path: '/app/dashboard',
            name: '运行报表',
            icon: <DashboardTwoTone/>,
            component: Dashboard,
        },
        {
            path: '/app/task',
            name: '任务管理',
            icon: <DatabaseTwoTone/>,
            component: Task,
        },
        {
            path: '/app/log',
            name: '调度日志',
            icon: <CalendarTwoTone/>,
            component: Log,
        },
        {
            path: '/app/executor',
            name: '执行器管理',
            icon: <SettingTwoTone/>,
            component: Executor,
        },
        {
            path: '/app/user',
            name: '用户管理',
            icon: <SmileTwoTone/>,
            component: User,
        }
    ]

}
