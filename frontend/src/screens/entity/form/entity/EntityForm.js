import TextInput from "../../../../components/ui/input/TextInput";
import ColumnForm from "./ColumnForm";
import ModalWindow from "../../../../components/ui/container/ModalWindow";
import CardWindow from "../../../../components/ui/container/CardWindow";
import {Button} from "react-bootstrap";

function EntityForm({setEntities, index, entity}) {

    const initColumn = {
        "type": "",
        "name": "",
        "isUnique": false,
        "isNotNull": false
    }

    function addColumn(event) {
        event.preventDefault()
        const val = {...entity}
        val.columns.push(initColumn)
        setEntities(val, index)
    }

    function handleColumnInputChange(value, i) {
        const data = {...entity}
        data.columns[i] = value
        setEntities(data, index)
    }

    function onChange(event) {
        const target = event.target
        const value = target.value
        const name = target.name
        const val = {...entity, [name]: value}
        setEntities(val, index)
    }

    return (
        <>
            <CardWindow titleName={'Описание сущности ' + entity.entityName}>
                <div>
                    <p> Первичный ключ: {entity.primaryKey}</p>
                    <p> Колонки: </p>
                    {entity.columns.map((col, i) => <p key={i}> {col.name} : {col.type} </p>)}
                </div>
                <ModalWindow name={'cущности ' + entity.entityName}>
                    <p> Основная информация о сущности </p>
                    <TextInput placeholder='Имя сущности' inputName='entityName' data={entity} onChange={onChange}/>
                    <TextInput placeholder='Имя первичного ключа' inputName='primaryKey' data={entity}
                               onChange={onChange}/>
                    <p> Дополнительные колонки </p>
                    {entity.columns.map((column, i) =>
                        <ColumnForm key={i} index={i} column={column} setColumns={handleColumnInputChange}/>
                    )
                    }
                    <Button variant="primary" onClick={addColumn}> Добавить колонку </Button>
                </ModalWindow>
            </CardWindow>
        </>
    )
}

export default EntityForm;