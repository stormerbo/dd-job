import './executor.less';
import {Pagination, Space, Table, Tag} from "antd";
import {useState, useEffect} from 'react';
import axios from "axios";

const {Column} = Table;
axios.defaults.withCredentials = true;

function Executor() {
    const [loading, setLoading] = useState(false);
    const [data, setData] = useState([]);
    const [total, setTotal] = useState(0);
    const [pagination, setPagination] = useState({current: 1, pageSize: 10});

    useEffect(() => {
        console.log(pagination)
        handleTableChange(pagination, {}, {})
    }, [pagination]);

    function fetch(params = {}) {
        setLoading(true);
        axios.post("http://localhost:8080/list-executor", {
            dataFrom: (params.pagination.current - 1) * params.pagination.pageSize,
            dataCount: params.pagination.pageSize
        }).then(response => {
            setLoading(false);
            setData(response.data.data);
            setTotal(response.data.totalRecord)
        })

    }

    function onShowSizeChange(current, pageSize) {
        onPageChange(current, pageSize)
    }

    function onPageChange(current, pageSize) {
        setPagination({current: current, pageSize: pageSize});
        fetch({pagination: pagination})
    }

    function handleTableChange(pagination, filters, sorter) {
        fetch({
            sortField: sorter.field,
            sortOrder: sorter.order,
            pagination,
            ...filters,
        });
    }

    return (
        <div>
            <Table dataSource={data} pagination={false} loading={loading}>
                <Column title="ID" dataIndex="executorId" key="executorId" width="10%"/>
                <Column title="执行器名称" dataIndex="executorName" key="executorName"/>
                <Column title="注册方式" dataIndex="registerType" key="registerType" render={(text, record) => {
                    if (text === 0) {
                        return "自动注册"
                    } else {
                        return "手动注册"
                    }
                }}/>
                <Column title="机器地址" dataIndex="executorMachineList" key="executorMachineList"
                        render={executorMachineList => (
                            executorMachineList.map(executorMachine => (
                                <div>
                                    <Tag color={executorMachine.status === 0 ? "red" : "green"}
                                         key={executorMachine.executorMachineId}>
                                        {executorMachine.ip + ":" + executorMachine.port}
                                    </Tag>
                                </div>
                            ))
                        )}/>
                <Column
                    title="Action"
                    key="action"
                    render={(text, record) => (
                        <Space size="middle">
                            <a href="javascript:void(0);">编辑</a>
                            <a href="javascript:void(0);">删除</a>
                        </Space>
                    )}
                />
            </Table>
            <Pagination size="small" showSizeChanger showQuickJumper hideOnSinglePage
                        defaultCurrent={1}
                        total={total}
                        showTotal={() => `总共${total}条数据`}
                        onShowSizeChange={onShowSizeChange}
                        onChange={onPageChange}
            />
        </div>
    );
}

export default Executor;
