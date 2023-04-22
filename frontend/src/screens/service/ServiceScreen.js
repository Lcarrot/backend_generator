import {useEffect, useState} from "react";
import {DefaultMethods} from "../default/DefaultMethods";
import styles from "../entity/Entity.module.scss";
import ServiceForm from "./form/ServiceForm";
import DefaultHeader from "../../components/ui/container/DefaultHeader";
import DefaultButtonGroup from "../../components/ui/container/DefaultButtonGroup";
import {useNavigate} from "react-router-dom";

function ServiceScreen() {

    const navigate = useNavigate()

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetch('http://localhost:8080/repositories/get')
            const data = await response.json()
            setRepos(data)
        }
        fetchData().then(() => console.log('data was loaded'))
    }, [])

    const save = (e) => {
        e.preventDefault()
        console.log(JSON.stringify(services))
        const requestOptions = {
            method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(services)
        }
        fetch('http://localhost:8080/service/save', requestOptions)
            .then(async response => {
                console.log(response.ok)
                setIsOk(response.ok)
                if (response.ok) {
                    navigate('/controller/constructor', {replace: true})
                }
            })
    }

    const initService = {
        name: '',
        methods: []
    }

    const [isOk, setIsOk] = useState(false)
    const [services, setServices] = useState([])
    const [repos, setRepos] = useState([])

    const addService = DefaultMethods.addByClick(services, initService, setServices)

    const handleService = DefaultMethods.defaultArrayChangeHandler(services, setServices)

    return (
        <>
            <DefaultHeader pageName='Описание сервисов'>
                <DefaultButtonGroup addInfo={{onClick: addService, placeholder: 'Добавить сервис'}}
                                    saveInfo={{onClick: save, placeholder: 'Сохранить'}} isOk={isOk}
                />
            </DefaultHeader>
            <div className={styles.entity}>
                {services.map((service, index) => <ServiceForm key={index}
                                                               index={index}
                                                               service={services[index]}
                                                               setService={handleService}
                                                               repos={repos}/>)}
            </div>
        </>)
}

export default ServiceScreen;