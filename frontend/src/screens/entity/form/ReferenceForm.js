import ObjectInput from "../../../components/ui/input/ObjectInput";
import DropdownInput from "../../../components/ui/input/DropdownInput";
import styles from "../Entity.module.scss";
import {useState} from "react";

function ReferenceForm({onChange, index}) {

    const referenceTypes = ['ONE_TO_MANY', 'MANY_TO_ONE']

    const initReference = {
        "entityTo": "",
        "entityFrom": "",
        "referenceType": "ONE_TO_MANY"
    }

    const [reference, setReference] = useState(initReference)

    function onChangeValue(event) {
        const target = event.target
        const value = target.value
        const name = target.name
        const val = {...reference, [name]: value}
        setReference(val)
        onChange(val, index)
    }

    return (
        <div className={styles.entityForm}>
            <h2>Описание связи сущностей</h2>
            <ObjectInput placeholder='Название сущности' inputName="entityTo" data={reference}
                         onChange={onChangeValue}/>
            <DropdownInput values={referenceTypes} onChange={onChangeValue} name='referenceType' currentValue={reference}/>
            <ObjectInput placeholder='Название сущности' inputName='entityFrom' data={reference}
                         onChange={onChangeValue}/>
        </div>)
}

export default ReferenceForm;