import {useState} from "react";
import styles from "./InputForm.module.scss";
import ObjectInput from "../../../components/ui/input/ObjectInput";

function IndexForm({isOk}) {


    const initData = {
        "projectPath": "",
        "projectPacket": "",
        "projectName": "",
        "db_url": "",
        "db_password": "",
        "db_user": ""
    }

    const [data, setData] = useState(initData)

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
                isOk(response.ok)
                setData(initData)
            })
    }

    function handleInputChange(event) {
        const target = event.target
        const value = target.value
        const name = target.name
        setData(prev => ({...prev, [name]: value}))
    }

    return (
        <div className='formContainer'>
            <form className={styles.form}>
                <ObjectInput placeholder='Путь до проекта' inputName='projectPath' data={data}
                             onChange={handleInputChange}/>
                <ObjectInput placeholder='Внутренний пакет' inputName='projectPacket' data={data}
                             onChange={handleInputChange}/>
                <ObjectInput placeholder='Название проекта' inputName='projectName' data={data}
                             onChange={handleInputChange}/>
                <h1>Информация для подключения к бд (postgresql)</h1>
                <ObjectInput placeholder='URL для подключения к бд' inputName='db_url' data={data}
                             onChange={handleInputChange}/>
                <ObjectInput placeholder='Пароль для подключения к бд' inputName='db_password' data={data}
                             onChange={handleInputChange}/>
                <ObjectInput placeholder='Пользователь для подключения к бд' inputName='db_user' data={data}
                             onChange={handleInputChange}/>
                <button className={styles.button} onClick={(e) => save(e)}>Найти</button>
            </form>
        </div>
    )
}

export default IndexForm;