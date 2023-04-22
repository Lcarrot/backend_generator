import DropdownInput from "../../../components/ui/input/DropdownInput";
import RepositoryMethodCondition from "./RepositoryMethodCondition";
import {Button} from "react-bootstrap";

function RepositoryMethodForm({setMethod, method, index, entity}) {

    const RETURN_TYPES = ['', 'ENTITY_LIST', 'ENTITY']

    const initCondition = {
        field: "",
        operationCondition: "",
        link: ""
    }

    const onChangeTextFieldRepo = (event) => {
        const data = {...method, [event.target.name]: event.target.value}
        setMethod(data, index)
    }

    const onChangeCondition = (value, i) => {
        const data = {...method}
        data.conditions[i] = value
        setMethod(data, index)
    }

    const addCondition = (event) => {
        event.preventDefault()
        const data = {...method}
        data.conditions.push(initCondition)
        setMethod(data, index)
    }

    return (
        <>
            <p> Описание метода </p>
            <DropdownInput values={RETURN_TYPES} placeholder='Возвращаемый тип'
                           onChange={onChangeTextFieldRepo} name='returnType'/>
            <p> Описание условий фильтрации </p>
            {method.conditions.map((condition, i) =>
                <RepositoryMethodCondition
                    columns={entity.columns} index={i} key={i}
                    condition={condition}
                    setCondition={onChangeCondition}/>)}
            <Button variant="primary" onClick={addCondition}> Добавить условие </Button>
        </>)
}

export default RepositoryMethodForm;