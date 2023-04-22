import TextInput from "../../../components/ui/input/TextInput";
import DropdownInput from "../../../components/ui/input/DropdownInput";

function ControllerMethodForm({method, setMethod, index, serviceMethods}) {

    const handleTextInput = (event) => {
        event.preventDefault()
        const data = {...method, [event.target.name]: event.target.value}
        setMethod(data, index)
    }

    return (
        <div>
            <TextInput data={method} inputName='name' placeholder='Введите имя метода' onChange={handleTextInput}/>
            <TextInput data={method} inputName='path' placeholder='Введите внешний путь к ресурсу'
                       onChange={handleTextInput}/>
            <DropdownInput values={['', ...serviceMethods]} name='serviceMethod' onChange={handleTextInput}
                           placeholder='Выберите метод сервиса'/>
        </div>)
}

export default ControllerMethodForm;