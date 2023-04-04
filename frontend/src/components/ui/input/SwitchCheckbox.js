import {Form} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

function SwitchCheckbox({label, name, onChange, value}) {
    return (
        <Form.Check
            name={name}
            value={value[name]}
            type="switch"
            label={label}
            id={crypto.randomUUID()}
            onChange={onChange}
        />)
}

export default SwitchCheckbox;