
interface User {
    id: number;
    username: string;
    email: string;
    active: boolean;
}

interface UserManagementProps {
    users: User[];
    onActivate: (userId: number) => void;
    onDeactivate: (userId: number) => void;
}

const UserManagement: React.FC<UserManagementProps> = ({ users, onActivate, onDeactivate }) => {
    return (
        <div className="bg-white p-6 rounded-lg shadow-md">
            <h2 className="text-xl font-bold text-slate-700 mb-4">Управление пользователями</h2>
            <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                    <tr>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Имя пользователя
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Email
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Статус
                        </th>
                        <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                            Действия
                        </th>
                    </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                    {users.map((user) => (
                        <tr key={user.id}>
                            <td className="px-6 py-4 whitespace-nowrap">{user.username}</td>
                            <td className="px-6 py-4 whitespace-nowrap">{user.email}</td>
                            <td className="px-6 py-4 whitespace-nowrap">
                                {user.active ? (
                                    <span className="text-green-500">Активный</span>
                                ) : (
                                    <span className="text-red-500">Неактивный</span>
                                )}
                            </td>
                            <td className="px-6 py-4 whitespace-nowrap">
                                {user.active ? (
                                    <button
                                        onClick={() => onDeactivate(user.id)}
                                        className="bg-red-500 text-white py-1 px-2 rounded-lg hover:bg-red-600 transition duration-300"
                                    >
                                        Деактивировать
                                    </button>
                                ) : (
                                    <button
                                        onClick={() => onActivate(user.id)}
                                        className="bg-green-500 text-white py-1 px-2 rounded-lg hover:bg-green-600 transition duration-300"
                                    >
                                        Активировать
                                    </button>
                                )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default UserManagement;