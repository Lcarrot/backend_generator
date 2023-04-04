import {Form} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

function DropdownInput({values, onChange, name, currentValue}) {

    return (
        <div>
            <Form.Select aria-label="Default select example" onChange={onChange} value={currentValue[name]} name={name}>
                {values.map((val, index) => <option value={val} key={index}>{val}</option>)}
            </Form.Select>
        </div>)
}

export default DropdownInput;