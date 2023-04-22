import {useState} from "react";
import {useNavigate} from "react-router-dom";
import EntityForm from "./form/entity/EntityForm";
import styles from "./Entity.module.scss";
import {Button, ButtonGroup} from "react-bootstrap";
import ReferenceForm from "./form/ReferenceForm";
import DefaultHeader from "../../components/ui/container/DefaultHeader";

function EntityScreen() {

    const initData = {
        "entityName": "", "primaryKey": "", columns: []
    }

    const initRef = {
        entityTo: "", referenceType: "", entityFrom: ""
    }

    const navigate = useNavigate();

    const [entityForms, setEntityForms] = useState([])
    const entityFormsNames = entityForms.map(entityForm => entityForm.entityName)

    const [references, setReferences] = useState([])

    const [isOk, setIsOk] = useState(false)
    const save = (e) => {
        e.preventDefault()
        const body = JSON.stringify({entities: entityForms, references: references});
        console.log(body)
        const requestOptions = {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: body
        }
        fetch('http://localhost:8080/entities/save', requestOptions)
            .then(async response => {
                console.log(response.ok)
                setIsOk(response.ok)
                if (response.ok) {
                    navigate('/repository/constructor', {replace: true})
                }
            })
    }

    const handleEntityForm = (form, index) => {
        const newData = [...entityForms]
        newData[index] = form
        setEntityForms(newData)
    }

    const handleReference = (ref, index) => {
        const newData = [...references]
        newData[index] = ref
        setReferences(newData)
    }

    const addEntity = (event) => {
        event.preventDefault()
        const values = [...entityForms]
        values.push(initData)
        setEntityForms(values)
    }

    const addReference = (event) => {
        event.preventDefault()
        const values = [...references]
        values.push(initRef)
        setReferences(values)
    }

    return (<>
        <DefaultHeader pageName='Описание сущностей'>
            <ButtonGroup vertical={false}>
                <Button variant="outline-light" size='lg' onClick={addEntity}> Добавить сущность </Button>
                <Button variant='outline-light' size='lg' onClick={addReference}> Добавить связь сущностей </Button>
                <Button variant='outline-light' size='lg' hidden={isOk} onClick={save}> Сохранить
                    изменения </Button>
            </ButtonGroup>
        </DefaultHeader>
        <div className={styles.entity}>
            {entityForms.map((form, index) => <EntityForm key={index}
                                                          index={index}
                                                          entity={form}
                                                          setEntities={handleEntityForm}/>)}
        </div>
        <div className={styles.entity}>
            {references.map((reference, index) => <ReferenceForm key={index}
                                                                 onChange={handleReference}
                                                                 entityNames={entityFormsNames} index={index}
                                                                 reference={reference}/>)}
        </div>
    </>)
}

export default EntityScreen;