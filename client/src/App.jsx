import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Modules from './pages/Modules';
import ModuleDetail from './pages/ModuleDetail';
import ModuleNew from './pages/ModuleNew';
import ModuleEdit from './pages/ModuleEdit';
import ModuleCSVUpload from './pages/ModuleCSVUpload';
import AssessmentDetail from './pages/AssessmentDetail';
import Test from './pages/Test';
import Coursework from './pages/Coursework';
import Exam from './pages/Exam';
import AssessmentNew from './pages/AssessmentNew';
import UserManagement from './pages/UserManagement';
import UserNew from './pages/UserNew';
import MyProfile from './pages/MyProfile';
import FeedbackTest from './pages/dev/FeedbackTest';
import ModuleNewTest from './pages/dev/ModuleNewTest';
import ModuleCSVUploadTest from './pages/dev/ModuleCSVUploadTest';

import './App.css';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/modules" element={<Modules />} />
        <Route path="/modules/new" element={<ModuleNew />} />
        <Route path="/modules/upload-csv" element={<ModuleCSVUpload />} />
        <Route path="/modules/:id" element={<ModuleDetail />} />
        <Route path="/modules/:id/edit" element={<ModuleEdit />} />
        <Route path="/modules/:moduleId/assessments/new" element={<AssessmentNew />} />
        <Route path="/modules/:moduleId/assessments/:assessmentId" element={<AssessmentDetail />} />
        <Route path="/modules/:moduleId/test/:assessmentId" element={<Test />} />
        <Route path="/modules/:moduleId/coursework/:assessmentId" element={<Coursework />} />
        <Route path="/modules/:moduleId/exam/:assessmentId" element={<Exam />} />
        <Route path="/users" element={<UserManagement />} />
        <Route path="/users/new" element={<UserNew />} />
        <Route path="/profile" element={<MyProfile />} />

        {/* Dev-only routes */}
        {process.env.NODE_ENV === 'development' && (
      <>
       <Route path="/test/module-new" element={<ModuleNewTest />} />
       <Route path="/test/module-upload" element={<ModuleCSVUploadTest />} />
       <Route path="/test/feedback" element={<FeedbackTest />} />
       <Route path="/test" element={<Test />} />
    </>
  )}
</Routes>
    </BrowserRouter>
  );
}

export default App;
