import {useState} from "react";
import {Button, Modal} from "react-bootstrap";


function ModalWindow({name, children}) {

    const [show, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
            <Button variant="primary" onClick={handleShow}> Открыть окно редактирования </Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header>
                    <Modal.Title> Редактирование {name}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {children}
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>Применить</Button>
                </Modal.Footer>
            </Modal>
        </>)
}

export default ModalWindow;