import TextInput from "../../../components/ui/input/TextInput";
import DropdownInput from "../../../components/ui/input/DropdownInput";
import {useState} from "react";
import ControllerMethodForm from "./ControllerMethodForm";
import {Button} from "react-bootstrap";
import CardWindow from "../../../components/ui/container/CardWindow";
import ModalWindow from "../../../components/ui/container/ModalWindow";

function ControllerForm({controller, setController, index, services}) {

    const initMethod = {
        name: "",
        path: "",
        serviceMethod: ""
    }

    const [service, setService] = useState({name: '', methods: ['']})

    const inputTextChange = (event) => {
        event.preventDefault()
        const data = {...controller, [event.target.name]: event.target.value}
        setController(data, index)
    }

    const changeService = (event) => {
        event.preventDefault()
        const curServ = services.filter(serv => serv.name === event.target.value)[0]
        setService(curServ)
        const data = {...controller, 'service': event.target.value}
        setController(data, index)
    }

    const handlerChangeMethod = (val, ind) => {
        console.log(val)
        const data = {...controller}
        data.methods[ind] = val
        setController(data, index)
    }

    const addMethod = (event) => {
        event.preventDefault()
        const data = {...controller}
        data.methods.push(initMethod)
        setController(data, index)
    }

    return (
        <div>
            <CardWindow titleName={'Описание контроллера: ' + controller.name}>
                <p> Мктоды: </p>
                {controller.methods.map(method => <p> {method.name} </p>)}
                <ModalWindow name={'контроллера ' + controller.name}>
                    <TextInput inputName='name' data={controller} placeholder='Введите имя контроллера'
                               onChange={inputTextChange}/>
                    <DropdownInput name='service' placeholder='Выберите сервис'
                                   values={['', ...services.map(serv => serv.name)]}
                                   onChange={changeService} value={controller}/>
                    {controller.methods.map((method, ind) => <ControllerMethodForm key={ind} method={method} index={ind}
                                                                                   setMethod={handlerChangeMethod}
                                                                                   serviceMethods={service.methods}/>)}
                    <Button variant="secondary" size='lg' onClick={addMethod}> Добавить метод </Button>
                </ModalWindow>
            </CardWindow>
        </div>)
}

export default ControllerForm;