import styles from "./Input.module.scss";
import {FloatingLabel, Form} from "react-bootstrap";

function TextInput({placeholder, inputName, data, onChange}) {

    return (
        <div className={styles.input}>
            <FloatingLabel label={placeholder}>
                <Form.Control placeholder={placeholder}
                              name={inputName}
                              value={data[inputName]}
                              onChange={onChange}/>
            </FloatingLabel>
        </div>)
}

export default TextInput;