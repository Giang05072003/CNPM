import { ArrowBackIosNew, StorageOutlined } from '@mui/icons-material';
import { Stack } from '@mui/material';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export function HeaderAdmin() {
    const [hovered, setHovered] = useState(false);
    const navigate = useNavigate();
    const handleMoveHome = () => {
        navigate('/', { replace: true });
    };
    const handleRouter = (router: string) => {
        navigate(`${router}`);
    };
  

    return (
        <Stack direction="row" className="p-[10px] border-b border-gray-300" justifyContent="space-between ">
            <Stack direction="row" alignItems="center">
                <Stack
                    direction="row"
                    alignItems={'center'}
                    onMouseEnter={() => setHovered(true)}
                    onMouseLeave={() => setHovered(false)}
                    onClick={handleMoveHome}
                    sx={{
                        cursor: 'pointer',
                        margin: '0 5px',

                        '&:hover': {
                            transform: ' translateX(-5px)',
                            transition: 'all 0.3s'
                        }
                    }}
                >
                    <span className="mr-3">{hovered ? <ArrowBackIosNew /> : <StorageOutlined />}</span>
                    <span className="font-medium mr-2">Trang quản trị</span>
                </Stack>

                <div className="relative">
                    <button
                        onClick={() => handleRouter('customer')}
                        className="bg-white translate-y-[1.5px] hover:bg-gray-200 text-gray-800 text-[14px]  py-1 px-3  hover:border-gray-300 rounded mr-1"
                    >
                        Khách hàng
                    </button>
                </div>

                <div className="relative">
                    <button
                        onClick={() => handleRouter('invoice')}
                        className="bg-white translate-y-[1.5px] hover:bg-gray-200 text-gray-800 text-[14px]  py-1 px-3  hover:border-gray-300 rounded mr-1"
                    >
                        Hóa đơn
                    </button>
                    {/* <MenuAdmin open={openInvoice} setOpen={setOpenInvoice} items={InvoiceList} /> */}
                </div>
                <div className="relative">
                    <button
                        onClick={() => handleRouter('products')}
                        className="bg-white translate-y-[1.5px] hover:bg-gray-200 text-gray-800 text-[14px]  py-1 px-3  hover:border-gray-300 rounded mr-1"
                    >
                        Sản phẩm
                    </button>
                </div>
            </Stack>
        </Stack>
    );
}
