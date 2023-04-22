import {useEffect, useState} from "react";
import {DefaultMethods} from "../default/DefaultMethods";
import styles from "../entity/Entity.module.scss";
import ControllerForm from "./form/ControllerForm";
import DefaultHeader from "../../components/ui/container/DefaultHeader";
import DefaultButtonGroup from "../../components/ui/container/DefaultButtonGroup";
import {useNavigate} from "react-router-dom";

function ControllerScreen() {

    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetch('http://localhost:8080/service/get')
            const data = await response.json()
            setServices(data)
        }
        fetchData().then(() => console.log('data was loaded'))
    }, [])

    const save = (e) => {
        e.preventDefault()
        console.log(JSON.stringify(controllers))
        const requestOptions = {
            method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(controllers)
        }
        fetch('http://localhost:8080/controllers/save', requestOptions)
            .then(async response => {
                console.log(response.ok)
                setIsOk(response.ok)
                if (response.ok) {
                    navigate('/final', {replace: true})
                }
            })
    }

    const initController = {
        name: '',
        service: '',
        methods: []
    }

    const [isOk, setIsOk] = useState(false)
    const [services, setServices] = useState([])
    const [controllers, setControllers] = useState([])

    const addController = DefaultMethods.addByClick(controllers, initController, setControllers)

    const handleController = DefaultMethods.defaultArrayChangeHandler(controllers, setControllers)

    return (
        <>
            <DefaultHeader pageName='Описание контроллеров'
                           addInfo={{onClick: addController, placeholder: 'Добавить контроллер'}}
                           saveInfo={{onClick: save, placeholder: 'Сохранить'}} isOk={isOk}>
                <DefaultButtonGroup addInfo={{onClick: addController, placeholder: 'Добавить контроллер'}}
                                    saveInfo={{onClick: save, placeholder: 'Сохранить'}} isOk={isOk}/>
            </DefaultHeader>
            <div className={styles.entity}>
                {controllers.map((controller, index) => <ControllerForm key={index}
                                                                        index={index}
                                                                        controller={controller}
                                                                        setController={handleController}
                                                                        services={services}/>)}
            </div>
        </>)
}

export default ControllerScreen;