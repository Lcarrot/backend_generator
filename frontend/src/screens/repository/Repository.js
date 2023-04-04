import {Button, ButtonGroup} from "react-bootstrap";
import {Link} from "react-router-dom";
import {useState} from "react";
import styles from "../entity/Entity.module.scss";
import RepositoryForm from "./form/RepositoryForm";

function Repository() {

    const [isOk, setIsOk] = useState(false)
    const [repos, setRepos] = useState([])

    function addRepo(event) {
        event.preventDefault()
        const values = [...repos]
        values.push({})
        setRepos(values)
    }

    function handleRepo(form, index) {
        const newData = [...repos]
        newData[index] = form
        setRepos(newData)
        console.log(newData)
    }

    return (
        <div>
            <h1>Описание репозиториев</h1>
            <ButtonGroup vertical={true}>
                <Button variant="secondary" size='lg' onClick={e => addRepo(e)}> Добавить репозиторий</Button>
                <Link to={"/entity/constructor"} hidden={isOk === false}> Перейти на след раздел </Link>
            </ButtonGroup>
            <div className={styles.entity}>
                {repos.map((form, index) => <RepositoryForm key={index}
                                                            index={index}
                                                            setRepos={handleRepo}/>)}
            </div>
        </div>)
}

export default Repository;