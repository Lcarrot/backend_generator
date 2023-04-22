import styles from "./InputForm.module.scss";
import TextInput from "../../../components/ui/input/TextInput";

function IndexForm({data, setData}) {

    function handleInputChange(event) {
        const target = event.target
        const value = target.value
        const name = target.name
        setData(prev => ({...prev, [name]: value}))
    }

    return (
        <div className='formContainer'>
            <form className={styles.form}>
                <h1> Основная информация о проекте</h1>
                <TextInput placeholder='Путь до проекта' inputName='projectPath' data={data}
                           onChange={handleInputChange}/>
                <TextInput placeholder='Внутренний пакет' inputName='projectPacket' data={data}
                           onChange={handleInputChange}/>
                <TextInput placeholder='Название проекта' inputName='projectName' data={data}
                           onChange={handleInputChange}/>
                <h1>Информация для подключения к бд (postgresql)</h1>
                <TextInput placeholder='URL для подключения к бд' inputName='db_url'
                           data={data}
                           onChange={handleInputChange}/>
                <TextInput placeholder='Пароль для подключения к бд' inputName='db_password' data={data}
                           onChange={handleInputChange}/>
                <TextInput placeholder='Пользователь для подключения к бд' inputName='db_user' data={data}
                           onChange={handleInputChange}/>
            </form>
        </div>
    )
}

export default IndexForm;