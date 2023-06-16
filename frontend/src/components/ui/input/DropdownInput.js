import {Form} from 'react-bootstrap';
import styles from "./Input.module.scss";
import 'bootstrap/dist/css/bootstrap.min.css';

function DropdownInput({values, onChange, name, placeholder, value}) {

    return (
        <div className={styles.input}>
            <Form.Label htmlFor={name}> {placeholder} </Form.Label>
            <Form.Select aria-label="Default select example" onChange={onChange} name={name} value={value[name]}>
                {values.map((val, index) => <option value={val} key={index}>{val}</option>)}
            </Form.Select>
        </div>)
}

export default DropdownInput;