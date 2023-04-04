import ObjectInput from "../../../components/ui/input/ObjectInput";
import DropdownInput from "../../../components/ui/input/DropdownInput";
import SwitchCheckbox from "../../../components/ui/input/SwitchCheckbox";
import {useState} from "react";

function ColumnForm({setColumns, index}) {

    const columnTypes = ['INT', 'BIGINT', 'BIGSERIAL', 'TEXT', 'BOOLEAN', 'DATE', 'TIMESTAMP', 'NUMERIC']

    const initColumn = {
        "type": "",
        "name": "",
        "isUnique": "",
        "isNotNull": ""
    }

    const [column, setColumn] = useState(initColumn)

    function onDataChange(event) {
        const target = event.target
        const value = target.value
        const name = target.name
        const newData = {...column, [name]: value}
        setColumn(newData)
        const retVal = {...newData}
        retVal.isUnique = newData.isUnique === 'on';
        retVal.isNotNull = newData.isNotNull === 'on';
        setColumns(retVal, index)
    }

    return (
        <div>
            <ObjectInput placeholder='Имя колонки' inputName='name'
                         data={column} onChange={onDataChange}/>
            <DropdownInput values={columnTypes} title='Выберите тип колонки' name='type' currentValue={column}
                           onChange={onDataChange}/>
            <SwitchCheckbox label="Сделать поле уникальным" name='isUnique'
                            onChange={onDataChange} value={column}/>
            <SwitchCheckbox label="Запретить пустое поле (NULL)" name='isNotNull' value={column}
                            onChange={onDataChange}/>
        </div>
    )
}

export default ColumnForm;