import { Box, Stack, Typography } from '@mui/material';
import { useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import logo from '../../assets/logo.png';
import { Customer, InvoicePaging, Products } from '../../features/admin';
import NewProduct from '../../features/admin/Components/newProduct';
import UpdateProduct from '../../features/admin/Components/updateProduct';
import { HeaderAdmin } from '../Common/HeaderAdmin';

const WelComeAdmin = () => {
    return (
        <Stack className="h-[100%] bg-[#fffbf5]" alignItems="center" justifyContent="center">
            <div style={{ width: '250px' }}>
                <img src={logo} alt="logo" />
            </div>
            <p className="font-semibold text-xl mb-2">Chào mừng đến với trang quản trị</p>
            <Typography className="text-base text-slate-400">Hãy bắt đầu với những chức năng có sẵn. Chúc bạn một ngày tốt lành</Typography>
        </Stack>
    );
};

export default function Admin() {

    useEffect(() => {
        document.body.style.overflow = 'hidden';
        return () => {
            document.body.style.overflow = 'hidden scroll';
        };
    }, []);

    return (
        <Box className="h-screen w-screen">
            <HeaderAdmin />
            <Box sx={{ height: 'calc(100vh - 61px)' }} className="w-screen">
                <Routes>
                    <Route path="/" element={<WelComeAdmin />} />
                    <Route path="/customer" element={<Customer />} />
                    <Route path="/invoice" element={<InvoicePaging />} />
                    <Route path="/products" element={<Products />} />
                    <Route path="/new/product" element={<NewProduct />} />
                    <Route path="/update/product/:productId" element={<UpdateProduct />} />
                </Routes>
            </Box>
        </Box>
    );
}
