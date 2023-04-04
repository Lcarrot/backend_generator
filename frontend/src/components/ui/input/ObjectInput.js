import styles from "./Input.module.scss";

function ObjectInput({placeholder, inputName, data, onChange}) {

    return (
        <div>
            <input placeholder={placeholder} className={styles.input}
                   name={inputName}
                   value={data[inputName]}
                   onChange={onChange}/>
        </div>)
}

export default ObjectInput;