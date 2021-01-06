import './login.css'
//import {useState, useEffect} from 'react';
import {Button, Checkbox, Form, Input} from "antd";
import {UserOutlined, LockOutlined, EyeInvisibleOutlined, EyeTwoTone} from '@ant-design/icons';
import axios from "axios";

function Login(props) {
    //const [userLoginState, setUserLoginState] = useState<API.LoginStateType>({});
    function onFinish(values) {
        console.log(values)
        axios.post("http://localhost:8080/login", {
            ...values,
            "remember-me": values.remember ? "on" : "off"
        }).then(response => {
            if (response.data.code === 0) {
                props.history.push("/dashboard")
            } else {

            }
        })
    }

    return (
        <div className="login-form-container">
            <Form
                name="normal_login"
                className="login-form"
                initialValues={{remember: true}}
                onFinish={onFinish}
            >
                <Form.Item
                    name="username"
                    rules={[{required: true, message: 'Please input your Username!'}]}
                >
                    <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="Username"/>
                </Form.Item>
                <Form.Item
                    name="password"
                    rules={[{required: true, message: 'Please input your Password!'}]}
                >
                    <Input.Password
                        prefix={<LockOutlined className="site-form-item-icon"/>}
                        type="password"
                        placeholder="Password"
                        iconRender={visible => (visible ? <EyeTwoTone/> : <EyeInvisibleOutlined/>)}
                    />
                </Form.Item>
                <Form.Item>
                    <Form.Item name="remember" valuePropName="checked" noStyle>
                        <Checkbox>Remember me</Checkbox>
                    </Form.Item>

                    <a className="login-form-forgot" href="">
                        Forgot password
                    </a>
                </Form.Item>

                <Form.Item>
                    <Button type="primary" htmlType="submit" className="login-form-button">
                        Log in
                    </Button>
                    Or <a href="">register now!</a>
                </Form.Item>
            </Form>
        </div>
    );
}

export default Login;
