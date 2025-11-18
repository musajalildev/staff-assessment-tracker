import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Modules from './pages/Modules';
import ModuleDetail from './pages/ModuleDetail';
import ModuleNew from './pages/ModuleNew';
import ModuleEdit from './pages/ModuleEdit';
import AssessmentDetail from './pages/AssessmentDetail';
import AssessmentNew from './pages/AssessmentNew';
import UserManagement from './pages/UserManagement';
import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/modules" element={<Modules />} />
        <Route path="/modules/new" element={<ModuleNew />} />
        <Route path="/modules/:id" element={<ModuleDetail />} />
        <Route path="/modules/:id/edit" element={<ModuleEdit />} />
        <Route path="/modules/:moduleId/assessments/new" element={<AssessmentNew />} />
        <Route path="/modules/:moduleId/assessments/:assessmentId" element={<AssessmentDetail />} />
        <Route path="/users" element={<UserManagement />} />
        <Route path="/" element={<Navigate to="/login" replace />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
