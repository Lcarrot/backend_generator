import IndexForm from "./input/IndexForm";
import {useState} from "react";
import {Link} from "react-router-dom";

function Index() {

    const [isOk, setIsOk] = useState(false)
    return (
        <div>
            <h1>Основная информация о прокете</h1>
            <IndexForm isOk={setIsOk}/>
            <Link to={"/entity/constructor"} hidden={isOk === false}> Перейти на след раздел </Link>
        </div>)
}

export default Index;