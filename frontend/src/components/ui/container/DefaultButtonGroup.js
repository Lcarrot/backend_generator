import {Button, ButtonGroup} from "react-bootstrap";

function DefaultButtonGroup({addInfo, saveInfo, isOk}) {

    return (
        <>
            <ButtonGroup vertical={false}>
                <Button variant="outline-light" size='lg' onClick={addInfo.onClick}> {addInfo.placeholder} </Button>
                <Button variant='outline-light' size='lg' hidden={isOk}
                        onClick={saveInfo.onClick}> {saveInfo.placeholder} </Button>
            </ButtonGroup>
        </>)
}

export default DefaultButtonGroup;