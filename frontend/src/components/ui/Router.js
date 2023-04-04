import {BrowserRouter, Route, Routes} from "react-router-dom";
import Index from "../../screens/index/Index";
import Entity from "../../screens/entity/Entity";

function PageRouter() {
    return <BrowserRouter>
        <Routes>
            <Route element={<Index />} path='/'/>
            <Route element={<Entity />} path='/entity/constructor'/>
            <Route element= {<div> not found </div>} path='*' />
        </Routes>
    </BrowserRouter>
}

export default PageRouter;