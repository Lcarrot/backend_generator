import {useState} from "react";
import DropdownInput from "../../../components/ui/input/DropdownInput";

function ServiceRepositoryMethodForm({repoMethod, setRepoMethod, index, repos}) {

    const [repo, setRepo] = useState({name : "", methods: [""]})

    const changeRepo = (event) => {
        event.preventDefault()
        const curRepo = repos.filter(rep => rep.name === event.target.value)[0]
        const data = {...repoMethod, 'repositoryName': curRepo.name, 'repositoryMethod': curRepo.methods[0]}
        setRepo(curRepo)
        setRepoMethod(data, index)
    }

    const changeMethod = (event) => {
        event.preventDefault()
        const data = {...repoMethod, 'repositoryMethod': event.target.value}
        setRepoMethod(data, index)
    }

    return (
        <>
            <DropdownInput values={['', ...repos.map(repo => repo.name)]} name='repositoryName' onChange={changeRepo}
                           placeholder='Выберите репозиторий' value={repoMethod}/>
            <DropdownInput values={repo.methods} name='repositoryMethod' onChange={changeMethod}
                           placeholder='Выберите репозиторий' value={repoMethod}/>
        </>)
}

export default ServiceRepositoryMethodForm;