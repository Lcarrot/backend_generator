import TextInput from "../../../../components/ui/input/TextInput";
import DropdownInput from "../../../../components/ui/input/DropdownInput";
import SwitchCheckbox from "../../../../components/ui/input/SwitchCheckbox";
import { memo } from 'react';

let ColumnForm = ({setColumns, index, column}) => {

    const columnTypes = ['', 'INT', 'BIGINT', 'BIGSERIAL', 'TEXT', 'BOOLEAN', 'DATE', 'TIMESTAMP', 'NUMERIC']

    const onSwitchCheckboxDataChange = (event) => {
        const target = event.target
        const value = target?.checked
        const name = target.name
        const newData = {...column, [name]: value}
        const retVal = {...newData}
        retVal.isUnique = !!newData.isUnique;
        retVal.isNotNull = !!newData.isNotNull;
        setColumns(retVal, index)
    }

    const onTextFieldChange = (event) => {
        const name = event.target.name
        const newData = {...column, [name]: event.target.value}
        setColumns(newData, index)
    }

    return (
        <div>
            <TextInput placeholder='Имя колонки' inputName='name'
                       data={column} onChange={onTextFieldChange}/>
            <DropdownInput values={columnTypes} title='Выберите тип колонки' name='type' currentValue={column}
                           onChange={onTextFieldChange}/>
            <SwitchCheckbox label="Сделать поле уникальным" name='isUnique'
                            onChange={onSwitchCheckboxDataChange} value={column.isUnique}/>
            <SwitchCheckbox label="Запретить пустое поле (NULL)" name='isNotNull' value={column.isNotNull}
                            onChange={onSwitchCheckboxDataChange}/>
        </div>
    )
}

ColumnForm = memo(ColumnForm);
export default ColumnForm;