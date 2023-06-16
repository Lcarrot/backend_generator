import IndexForm from "./input/IndexForm";
import {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import {Button, ButtonGroup} from "react-bootstrap";
import DefaultHeader from "../../components/ui/container/DefaultHeader";

function Index() {

    const navigate = useNavigate()

    const save = (e) => {
        e.preventDefault()
        const requestOptions = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        }
        fetch('http://localhost:8080/main/info', requestOptions)
            .then(async response => {
                console.log(response.ok)
                setIsOk(response.ok)
                if (response.ok) {
                    navigate('/entity/constructor', {replace: true})
                }
            })
    }

    const initData = {
        "projectPath": "",
        "projectPacket": "",
        "projectName": "",
        "db_url": "",
        "db_password": "",
        "db_user": ""
    }

    const [data, setData] = useState(initData)
    const [isOk, setIsOk] = useState(false)
    return (
        <>
            <DefaultHeader pageName='Создание нового проекта'>
                <ButtonGroup vertical={false}>
                    <Button variant='outline-light' size='lg' hidden={isOk} onClick={save}> Сохранить </Button>
                    <Link to={"/entity/constructor"} hidden={!isOk}>
                        <Button variant="outline-light" size='lg'> Перейти на след раздел</Button>
                    </Link>
                </ButtonGroup>
            </DefaultHeader>
            <IndexForm data={data} setData={setData}/>
        </>)
}

export default Index;