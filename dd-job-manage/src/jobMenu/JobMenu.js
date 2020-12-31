import './JobMenu.less'
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";
import {Menu} from "antd";
import {SettingTwoTone, DashboardTwoTone, DatabaseTwoTone, CalendarTwoTone, SmileTwoTone} from "@ant-design/icons";
import Dashboard from "../dashboard/Dashboard";
import Executor from "../executor/Executor";
import User from "../user/User";

function JobMenu() {
    return (
        <Router>
            <div>
                <Menu
                    defaultSelectedKeys={['1']}
                    defaultOpenKeys={['1']}
                    mode="inline"
                    theme="dark"
                >
                    <Menu.Item key="1" icon={<DashboardTwoTone/>}>
                        运行报表
                        <Link to="/"/>
                    </Menu.Item>
                    <Menu.Item key="2" icon={<DatabaseTwoTone />}>
                        任务管理
                        <Link to="/task"/>
                    </Menu.Item>
                    <Menu.Item key="3" icon={<CalendarTwoTone />}>
                        调度日志
                        <Link to="/log"/>
                    </Menu.Item>
                    <Menu.Item key="4" icon={<SettingTwoTone />}>
                        执行器管理
                        <Link to="/executor"/>
                    </Menu.Item>
                    <Menu.Item key="5" icon={<SmileTwoTone />}>
                        用户管理
                        <Link to="/user"/>
                    </Menu.Item>
                </Menu>
            </div>
            <Switch>
                <Route exact path="/">
                    <Dashboard/>
                </Route>
                <Route exact path="/executor">
                    <Executor/>
                </Route>
                <Route exact path="/user">
                    <User/>
                </Route>
            </Switch>
        </Router>
    );
}
export default JobMenu;
