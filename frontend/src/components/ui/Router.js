import {BrowserRouter, Route, Routes} from "react-router-dom";
import Index from "../../screens/index/Index";
import EntityScreen from "../../screens/entity/EntityScreen";
import RepositoryScreen from "../../screens/repository/RepositoryScreen";
import ServiceScreen from "../../screens/service/ServiceScreen";
import ControllerScreen from "../../screens/controller/ControllerScreen";

function PageRouter() {
    return <BrowserRouter>
        <Routes>
            <Route element={<Index />} path='/'/>
            <Route element={<EntityScreen />} path='/entity/constructor'/>
            <Route element={<RepositoryScreen />} path='/repository/constructor' />
            <Route element={<ServiceScreen />} path='/service/constructor' />
            <Route element={<ControllerScreen />} path='/controller/constructor' />
            <Route element= {<div> not found </div>} path='*' />
        </Routes>
    </BrowserRouter>
}

export default PageRouter;