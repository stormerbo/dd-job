import './JobMenu.css';
import {Layout, Menu} from 'antd';
import {MenuFoldOutlined, MenuUnfoldOutlined,} from "@ant-design/icons";
import {useState} from 'react'
import {Link, Route, Switch} from "react-router-dom";
import config from "../config";


const {Header, Sider, Content} = Layout;

function JobMenu() {

    const [collapsed, setCollapsed] = useState(false);

    function toggle() {
        setCollapsed(!collapsed);
    }

    function handleSelect(item) {
    }

    return (
        <div className="menu-layout">
            <Layout>
                <Sider trigger={null} collapsible collapsed={collapsed}>
                    <div className="logo"/>
                    <Menu theme="dark" mode="inline" defaultSelectedKeys={['1']} onClick={handleSelect}>
                        {
                            config.routes.map((el, index) => (
                                <Menu.Item key={index} icon={el.icon}>
                                    <Link to={el.path}>{el.name}</Link>
                                </Menu.Item>
                            ))
                        }
                    </Menu>
                </Sider>
                <Layout className="site-layout">
                    <Header className="site-layout-background">
                        {collapsed ? <MenuUnfoldOutlined className="trigger" onClick={toggle}/> :
                            <MenuFoldOutlined className="trigger" onClick={toggle}/>}
                    </Header>
                    <Content className="site-layout-background">
                        <Switch>
                            {
                                config.routes.map(router => (
                                    <Route
                                        path={router.path}
                                        component={router.component}
                                    />
                                ))
                            }
                        </Switch>
                    </Content>
                </Layout>
            </Layout>
        </div>
    );
}

export default JobMenu;
