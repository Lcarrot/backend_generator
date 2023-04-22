import TextInput from "../../../components/ui/input/TextInput";
import {Button} from "react-bootstrap";
import ServiceMethodForm from "./ServiceMethodForm";
import ModalWindow from "../../../components/ui/container/ModalWindow";
import CardWindow from "../../../components/ui/container/CardWindow";

function ServiceForm({service, setService, index, repos}) {

    const initMethod = {
        name: "",
        repositoryMethods: []
    }

    const onTextInputChange = (event) => {
        const data = {...service, [event.target.name]: event.target.value}
        setService(data, index)
    }

    const onMethodChange = (method, i) => {
        const data = {...service}
        data.methods[i] = method
        setService(data, index)
    }

    const addMethod = (event) => {
        event.preventDefault()
        let current = {...service}
        current.methods.push(initMethod)
        setService(current, index)
    }

    return (
        <div>
            <CardWindow titleName={'Описание сервиса ' + service.name}>
                <p> Методы: </p>
                {service.methods.map(method => <p> {method.name} </p>)}
                <ModalWindow name={service.name}>
                    <div>
                        <TextInput placeholder='Введите имя сервиса' data={service} inputName='name'
                                   onChange={onTextInputChange}/>
                        {service.methods.map((method, i) => <ServiceMethodForm key={i}
                                                                               method={method}
                                                                               index={i}
                                                                               repos={repos}
                                                                               setMethod={onMethodChange}/>)}
                        <Button variant="secondary" size='sm' onClick={addMethod}> Добавить метод </Button>
                    </div>
                </ModalWindow>
            </CardWindow>
        </div>)
}

export default ServiceForm;