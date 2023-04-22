import TextInput from "../../../components/ui/input/TextInput";
import ServiceRepositoryMethodForm from "./ServiceRepositoryMethodForm";
import {Button} from "react-bootstrap";

function ServiceMethodForm({method, setMethod, index, repos}) {

    const initRepoMethod = {
        repositoryName: "",
        repositoryMethod: ""
    }

    const changeText = (event) => {
        event.preventDefault()
        const data = {...method, [event.target.name]: event.target.value}
        setMethod(data, index)
    }

    const handleRepoMethod = (val, ind) => {
        const data = {...method}
        data.repositoryMethods[ind] = val
        setMethod(data, index)
    }

    const addRepMethod = (event) => {
        event.preventDefault()
        const values = {...method}
        values.repositoryMethods.push(initRepoMethod)
        setMethod(values, index)
    }

    return (
        <>
            <TextInput inputName='name' placeholder='Введите имя метода' data={method} onChange={changeText}/>
            {method.repositoryMethods.map((repMethod, ind) =>
                <ServiceRepositoryMethodForm
                    key={ind}
                    index={ind}
                    repos={repos}
                    repoMethod={repMethod}
                    setRepoMethod={handleRepoMethod}/>)}
            <Button variant="secondary" size='sm' onClick={addRepMethod}> Добавить метод репозитория </Button>
        </>)
}

export default ServiceMethodForm;