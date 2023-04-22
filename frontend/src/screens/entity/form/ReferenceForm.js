import DropdownInput from "../../../components/ui/input/DropdownInput";
import styles from "../Entity.module.scss";

function ReferenceForm({onChange, index, entityNames, reference}) {

    const referenceTypes = ['', 'ONE_TO_MANY']

    function onChangeValue(event) {
        const target = event.target
        const value = target.value
        const name = target.name
        const val = {...reference, [name]: value}
        onChange(val, index)
    }

    return (
        <div className={styles.entityForm}>
            <h2>Описание связи сущностей</h2>
            <DropdownInput values={["", ...entityNames]} placeholder='Название сущности' onChange={onChangeValue} name='entityTo'/>
            <DropdownInput values={referenceTypes} onChange={onChangeValue} name='referenceType' currentValue={reference}/>
            <DropdownInput values={["", ...entityNames]} placeholder='Название сущности' onChange={onChangeValue} name='entityFrom'/>
        </div>)
}

export default ReferenceForm;