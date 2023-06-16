import DropdownInput from "../../../components/ui/input/DropdownInput";

function RepositoryMethodCondition({columns, setCondition, condition, index}) {

    const operationConditions = ['', 'EQUAL', 'EQUAL_IGNORE_CASE', 'CONTAINING', 'BEFORE', 'AFTER', 'BETWEEN', 'GREATER_THAN', 'LESS_THAN']
    const linkConditions = ['', 'AND', 'OR']

    const onChangeTextFieldRepo = (event) => {
        const data = {...condition, [event.target.name]: event.target.value}
        setCondition(data, index)
    }

    return (
        <div>
            <DropdownInput values={operationConditions} placeholder='Операция фильтрации'
                           onChange={onChangeTextFieldRepo}
                           name='operationCondition' value={condition}/>
            <DropdownInput values={linkConditions} placeholder='Связь со следующим условием'
                           onChange={onChangeTextFieldRepo}
                           name='link' value={condition}/>
            <DropdownInput values={['', ...columns]} placeholder='Поле сущности'
                           onChange={onChangeTextFieldRepo}
                           name='field' value={condition}/>
        </div>)
}

export default RepositoryMethodCondition;