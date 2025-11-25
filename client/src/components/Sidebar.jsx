import { Link, useLocation } from 'react-router-dom';

function Sidebar() {
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path ? 'active' : '';
  };

  return (
    <aside className="sidebar">
      <div className="brand">
        <div className="logo"></div>
        <div>Assessment Tool</div>
      </div>
      <nav className="nav">
        <Link to="/dashboard" className={isActive('/dashboard')}>
          Dashboard
        </Link>
        <Link to="/modules" className={isActive('/modules')}>
          Modules
        </Link>
        <Link to="/users" className={isActive('/users')}>
          Users
        </Link>
        <Link to="/profile" className={isActive('/profile')}>
          My Profile
        </Link>
        <Link to="/login">Logout</Link>
      </nav>
    </aside>
  );
}

export default Sidebar;

