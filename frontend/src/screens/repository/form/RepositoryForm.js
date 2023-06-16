import TextInput from "../../../components/ui/input/TextInput";
import DropdownInput from "../../../components/ui/input/DropdownInput";
import RepositoryMethodForm from "./RepositoryMethodForm";
import {useState} from "react";
import CardWindow from "../../../components/ui/container/CardWindow";
import ModalWindow from "../../../components/ui/container/ModalWindow";
import {Button} from "react-bootstrap";

function RepositoryForm({setRepos, index, repo, entities}) {

    const [entity, setEntity] = useState({})

    const initRepoMethod = {
        returnType: "",
        conditions: []
    }

    const entityNames = entities.map(entity => entity.name)

    const onChangeTextFieldRepo = (event) => {
        const data = {...repo, [event.target.name]: event.target.value}
        setRepos(data, index)
    }

    const onChangeEntity = (event) => {
        const data = {...repo, [event.target.name]: event.target.value}
        const curEntity = entities.filter(val => val.name === event.target.value)[0]
        setEntity(curEntity)
        setRepos(data, index)
    }

    const onChangeMethod = (value, i) => {
        const data = {...repo}
        data.methods[i] = value
        setRepos(data, index)
    }

    const addMethod = (event) => {
        event.preventDefault()
        let data = {...repo}
        data.methods.push(initRepoMethod)
        setRepos(data, index)
    }

    return (
        <CardWindow titleName={'Описание репозитория: ' + repo.name}>
            <ModalWindow>
                <TextInput placeholder='Название репозитория' inputName="name" data={repo}
                           onChange={onChangeTextFieldRepo}/>
                <DropdownInput values={["", ...entityNames]} placeholder='Название сущности'
                               onChange={onChangeEntity} name='entityName' value={repo}/>
                {repo.methods.map((method, i) => <RepositoryMethodForm method={method} key={i} index={i}
                                                                       setMethod={onChangeMethod} entity={entity}/>)}
                <Button variant="primary" onClick={addMethod}> Добавить метод </Button>
            </ModalWindow>
        </CardWindow>)
}

export default RepositoryForm;