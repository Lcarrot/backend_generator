import {useState} from "react";
import styles from "../../entity/Entity.module.scss";
import ObjectInput from "../../../components/ui/input/ObjectInput";
import DropdownInput from "../../../components/ui/input/DropdownInput";
import RepositoryMethodForm from "./RepositoryMethodForm";

function RepositoryForm({setRepos, index}) {

    const initRepo = {
        "name": "",
        "entityName": "",
        "repositoryMethods": []
    }

    const [repo, setRepo] = useState(initRepo)

    return (
        <div className={styles.entityForm}>
            <h2>Описание связи сущностей</h2>
            <ObjectInput placeholder='Название репозитория' inputName="name" data={repo}
                         onChange={e => setRepo(prev => ({...prev, [e.target.name]: e.target.value}))}/>
            <ObjectInput placeholder='Название сущности' inputName="name" data={repo}
                         onChange={e => setRepo(prev => ({...prev, [e.target.name]: e.target.value}))}/>
            {repo.repositoryMethods.map((method, i) => <RepositoryMethodForm />)}
        </div>)
}

export default RepositoryForm;