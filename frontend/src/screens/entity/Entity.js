import {useState} from "react";
import {Link} from "react-router-dom";
import EntityForm from "./form/EntityForm";
import styles from "./Entity.module.scss";
import {Button, ButtonGroup} from "react-bootstrap";
import ReferenceForm from "./form/ReferenceForm";

function Entity() {

    const [isOk, setIsOk] = useState(false)

    const [entityForms, setEntityForms] = useState([])

    const [references, setReferences] = useState([])

    const save = (e) => {
        e.preventDefault()
        const requestOptions = {
            method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(entityForms)
        }
        fetch('http://localhost:8080//entities/save', requestOptions)
            .then(async response => {
                console.log(response.ok)
                setIsOk(response.ok)
                setEntityForms([])
                setReferences([])
            })
    }

    function handleEntityForm(form, index) {
        const newData = [...entityForms]
        newData[index] = form
        setEntityForms(newData)
        console.log(newData)
    }

    function handleReference(ref, index) {
        const newData = [...references]
        newData[index] = ref
        setReferences(newData)
    }

    function addEntity(event) {
        event.preventDefault()
        const values = [...entityForms]
        values.push({})
        setEntityForms(values)
    }

    function addReference(event) {
        event.preventDefault()
        const values = [...references]
        values.push({})
        setReferences(values)
    }

    return (<div>
        <h1>Описание сущностей</h1>
        <ButtonGroup vertical={true}>
            <Button variant="secondary" size='lg' onClick={e => addEntity(e)}> Добавить сущность</Button>
            <Button variant="secondary" size='lg' onClick={e => addReference(e)}> Добавить связь </Button>
            <Button variant='primary' size="lg" onClick={(e) => save(e)}>Создать</Button>
            <Link to={"/entity/constructor"} hidden={isOk === false}> Перейти на след раздел </Link>
        </ButtonGroup>
        <div className={styles.entity}>
            {entityForms.map((form, index) => <EntityForm key={index}
                                                          index={index}
                                                          setEntities={handleEntityForm}/>)}
        </div>
        <div className={styles.entity}>
            {references.map((ref, index) => <ReferenceForm key={index}
                                                           onChange={handleReference} index={index}/>)}
        </div>
    </div>)
}

export default Entity;