import {useState} from "react";
import ObjectInput from "../../../components/ui/input/ObjectInput";
import ColumnForm from "./ColumnForm";
import styles from "../Entity.module.scss"

function EntityForm({setEntities, index}) {

    const initData = {
        "entityName": "", "primaryKey": "", columns: []
    }

    const initColumn = {
        "type": "",
        "name": "",
        "isUnique": "",
        "isNotNull": ""
    }

    const [entity, setEntity] = useState(initData)

    function addColumn(event) {
        event.preventDefault()
        const val = {...entity}
        val.columns.push(initColumn)
        setEntity(val)
        setEntities(val, index)
    }

    function handleColumnInputChange(value, i) {
        const data = {...entity}
        data.columns[i] = value
        setEntity(data)
        setEntities(data, index)
    }

    function onChange(event) {
        const target = event.target
        const value = target.value
        const name = target.name
        const val = {...entity, [name]: value}
        setEntity(val)
        setEntities(val, index)
    }

    return (
        <div className={styles.entityForm}>
            <h2>Описание новой сущности</h2>
            <ObjectInput placeholder='Имя сущности' inputName='entityName' data={entity} onChange={onChange}/>
            <ObjectInput placeholder='Имя первичного ключа' inputName='primaryKey' data={entity}
                         onChange={onChange}/>
            {entity.columns.map((column, i) =>
                <ColumnForm key={crypto.randomUUID()} index={i} setColumns={handleColumnInputChange}/>
            )
            }
            <button className={styles.button} onClick={e => addColumn(e)}> Добавить колонку</button>
        </div>
    )
}

export default EntityForm;