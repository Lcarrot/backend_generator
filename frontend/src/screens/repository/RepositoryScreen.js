import {useEffect, useState} from "react";
import RepositoryForm from "./form/RepositoryForm";
import {DefaultMethods} from "../default/DefaultMethods";
import DefaultHeader from "../../components/ui/container/DefaultHeader";
import DefaultButtonGroup from "../../components/ui/container/DefaultButtonGroup";
import {useNavigate} from "react-router-dom";

function RepositoryScreen() {

    const navigate = useNavigate()

    useEffect(() => {
        const fetchData = async () => {
            const response = await fetch('http://localhost:8080/entities/get')
            const data = await response.json()
            setEntities(data)
        }
        fetchData().then(() => console.log('data was loaded'))
    }, [])

    const save = (e) => {
        e.preventDefault()
        console.log(JSON.stringify(repos))
        const requestOptions = {
            method: "POST", headers: {"Content-Type": "application/json"}, body: JSON.stringify(repos)
        }
        fetch('http://localhost:8080/repositories/save', requestOptions)
            .then(async response => {
                console.log(response.ok)
                setIsOk(response.ok)
                if (response.ok) {
                    navigate('/service/constructor', {replace: true})
                }
            })
    }

    const initRepo = {
        name: '',
        entityName: '',
        methods: []
    }

    const [isOk, setIsOk] = useState(false)
    const [repos, setRepos] = useState([])
    const [entities, setEntities] = useState([])

    const addRepo = DefaultMethods.addByClick(repos, initRepo, setRepos)

    const handleRepo = (form, index) => {
        const newData = [...repos]
        newData[index] = form
        console.log(newData)
        setRepos(newData)
    }

    return (
        <>
            <DefaultHeader pageName='Описание репозиториев'>
                <DefaultButtonGroup addInfo={{onClick: addRepo, placeholder: 'Добавить репозиторий'}}
                                    saveInfo={{onClick: save, placeholder: 'Сохранить'}} isOk={isOk}/>
            </DefaultHeader>
            {repos.map((repo, index) => <RepositoryForm key={index}
                                                        index={index}
                                                        repo={repo}
                                                        setRepos={handleRepo}
                                                        entities={entities}/>)}
            {entities.map(val => <p> {val.entityName} </p>)}
        </>)
}

export default RepositoryScreen;