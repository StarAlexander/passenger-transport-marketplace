// components/LoadingScreen.tsx

interface LoadingScreenProps {
    isLoading: boolean;
}

const LoadingScreen: React.FC<LoadingScreenProps> = ({ isLoading }) => {
    if (!isLoading) return null;

    return (
        <div className="absolute inset-0 flex items-center justify-center bg-gray-900 bg-opacity-75 z-70">
            <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-blue-500"></div>
        </div>
    );
};

export default LoadingScreen;