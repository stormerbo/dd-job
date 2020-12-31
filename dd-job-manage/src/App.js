import './App.less';
import {Breadcrumb, Layout} from 'antd';

import {useState} from 'react';
import JobMenu from "./jobMenu/JobMenu";

const {Header, Content, Footer, Sider} = Layout;

function App() {

    const [collapsed, setCollapsed] = useState(false);
    return (
        <Layout  style={{ minHeight: '100vh' }}>
            <Sider
                breakpoint="lg"
                onCollapse={() => {
                    setCollapsed(!collapsed)
                }}
                collapsible
                collapsed={collapsed}
                triger={null}
            >
                <JobMenu/>
            </Sider>
            <Layout className="site-layout">
                <Header className="site-layout-background" style={{padding: 0}}>
                </Header>
                <Content style={{margin: '0 16px'}}>
                    <Breadcrumb style={{margin: '16px 0'}}>
                        <Breadcrumb.Item>User</Breadcrumb.Item>
                        <Breadcrumb.Item>Bill</Breadcrumb.Item>
                    </Breadcrumb>
                    <div className="site-layout-background" style={{padding: 24, minHeight: 360}}>
                        content
                    </div>
                </Content>
                <Footer style={{textAlign: 'center'}}>dd-job ©2020 Created by stormer.xia</Footer>
            </Layout>
        </Layout>
    );
}

export default App;
