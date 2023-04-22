import {Form} from 'react-bootstrap';
import styles from "./Input.module.scss";
import 'bootstrap/dist/css/bootstrap.min.css';

function SwitchCheckbox({label, name, onChange, value}) {
    return <>
        <Form.Check
            className={styles.input}
            label={label}
            name={name}
            checked={value}
            type="switch"
            onChange={onChange}
        />
    </>
}

export default SwitchCheckbox;