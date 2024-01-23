import { Delete } from '@mui/icons-material';
import { Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, IconButton, MenuItem, Select, Stack, Typography } from '@mui/material';
import 'dayjs/locale/vi';
import { child, get, getDatabase, ref, update } from 'firebase/database';
import { MaterialReactTable, type MRT_ColumnDef, type MRT_ColumnFiltersState, type MRT_PaginationState, type MRT_SortingState } from 'material-react-table';
import { useSnackbar } from 'notistack';
import { useEffect, useMemo, useRef, useState } from 'react';
import { database } from '../../firebase';
import { Invoice, } from '../../models';
import { formatCurrencyVND } from '../../utils';
import SettingMenu from './Components/SettingMenu';

interface CustomDialogProps {
    open: boolean;
    setOpen: (value: boolean) => void;
    orderSelect: Invoice | null;
    setOrderSelect: (value: Invoice | null) => void;
    setIsUpdate: (value: boolean) => void;
}

const CustomDialog: React.FC<CustomDialogProps> = ({ open, setOpen, orderSelect, setOrderSelect, setIsUpdate }) => {
    const handleClose = () => {
        setOpen(false);
        setOrderSelect(null);
    };
    const dbRef = ref(database);
    const db = getDatabase();
    const { enqueueSnackbar } = useSnackbar();
    const [selectedOption, setSelectedOption] = useState<string | null>(null);
    const handleSelectChange = (value: string) => {
        setSelectedOption(value);
    };

    const handleUpdateUser = async () => {
        const newData = {
            inv_status: selectedOption 
        };
        try {
            if (orderSelect) {
                const invoiceRef = ref(db, `invoice/${orderSelect.inv_id}`);
                await update(invoiceRef, newData); // Cập nhật dữ liệu
                setIsUpdate(true); // Đánh dấu để tải lại dữ liệu
                enqueueSnackbar('Cập nhật thành công', { variant: 'success' });
                setOpen(false);
            }
        } catch (error) {
            console.log(error);
            enqueueSnackbar('Có lỗi xảy ra khi cập nhật', { variant: 'error' });
        }
    };

    return (
        <Dialog open={open} onClose={handleClose} className="w-full">
            <DialogTitle className=" ">Thay đổi trạng thái của đơn hàng {orderSelect?.inv_id}</DialogTitle>
            <DialogContent className="my-2">
                <Select
                    className="w-full"
                    labelId="status-label"
                    id="status-select"
                    value={selectedOption || orderSelect?.inv_status}
                    onChange={(e) => handleSelectChange(e.target.value)}
                >
                    <MenuItem value="0">Chờ xác nhận</MenuItem>
                    <MenuItem value="1">Đã giao hàng</MenuItem>
                </Select>
            </DialogContent>
            <DialogActions style={{ display: 'flex', flexDirection: 'row', justifyContent: 'space-between', paddingLeft: '20px', paddingRight: '20px' }}>
                <Button onClick={handleClose} color="primary">
                    Close
                </Button>
                <Button onClick={handleUpdateUser} color="primary">
                    Cập nhật
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export const InvoicePaging = () => {
    const dbRef = ref(database);
    const [data, setData] = useState<Invoice[]>([]);
    const [isLoading, setIsLoading] = useState(false);
    const [opentDialog, setOpenDialog] = useState<boolean>(false);
    const [isUpdate, setIsUpdate] = useState<boolean>(false);
    const [orderSelect, setorderSelect] = useState<Invoice | null>(null);
    const [isError, setIsError] = useState(false);
    const [isRefetching, setIsRefetching] = useState(false);
    const [rowCount, setRowCount] = useState(0);
    const { enqueueSnackbar } = useSnackbar();
    const [columnFilters, setColumnFilters] = useState<MRT_ColumnFiltersState>([]);
    const [globalFilter, setGlobalFilter] = useState('');
    const [sorting, setSorting] = useState<MRT_SortingState>([]);
    const [pagination, setPagination] = useState<MRT_PaginationState>({
        pageIndex: 0,
        pageSize: 10
    });
    const [isDel, setIsDel] = useState(false);
    const [open, setOpen] = useState(false);
    const settingRef = useRef<HTMLButtonElement>(null);
    
    const handleSelectRows = (row: any) => {
        const idData = row.map((item: any) => item.original.inv_id);
        (async () => {
            try {
                // console.log(idData)
                // await adminApi.deleteFood(idData);
                // enqueueSnackbar('Xóa thành công', { variant: 'success' });
                // setIsDel((item) => !item);
            } catch (error) {
                enqueueSnackbar('Có lỗi xảy ra thử lại sau', { variant: 'error' });
                console.log(error);
            }
        })();
    };
    useEffect(() => {
        const fetchData = async () => {
            if (!data.length) {
                setIsLoading(true);
            } else {
                setIsRefetching(true);
            }
            try {
                get(child(dbRef, 'invoice'))
                    .then((snapshot) => {
                        if (snapshot.exists()) {
                            setData(Object.keys(snapshot.val()).map((key) => snapshot.val()[key]));
                        } else {
                            console.log('loi');
                        }
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            } catch (error) {
                setIsError(true);
                console.error(error);
                return;
            }
            setIsUpdate(false);
            setIsError(false);
            setIsLoading(false);
            setIsRefetching(false);
        };
        fetchData();
    }, [columnFilters, globalFilter, isDel, pagination.pageIndex, pagination.pageSize, sorting, isUpdate]);
    const columns = useMemo<MRT_ColumnDef<Invoice>[]>(
        () => [
            { accessorKey: 'inv_id', header: 'ID' },
            { accessorKey: 'user_id', header: 'ID Người mua' },
            { accessorKey: 'inv_status', header: 'Trạng thái đơn hàng', Cell: ({ cell }) => (cell.row.original.inv_status === '1' ? 'Đã giao hàng' : 'Chờ xác nhận') },
            { accessorKey: 'totalPrice', header: 'Giá trị đơn hàng', Cell: ({ cell }) => formatCurrencyVND(cell.getValue<string>()) }
        ],
        []
    );

    // Cell: ({ cell }) => (+cell.row.original.role === 1111 ? 'User' : 'Admin

    return (
        <Box sx={{ height: '100%' }}>
            <SettingMenu anchorRef={settingRef} open={open} setOpen={setOpen} />
            <CustomDialog open={opentDialog} setOpen={setOpenDialog} setIsUpdate={setIsUpdate} orderSelect={orderSelect} setOrderSelect={setorderSelect} />
            <MaterialReactTable
                muiTablePaperProps={{ sx: { height: '100%' } }}
                muiTableContainerProps={{ sx: { height: 'calc(100%)' } }}
                columns={columns}
                data={data}
                // enableRowSelection // ẩn tích
                manualFiltering
                manualPagination
                muiTableBodyRowProps={({ row }) => ({
                    onClick: () => {
                        setorderSelect(row.original);
                        setOpenDialog(true);
                    },
                    sx: { cursor: 'pointer' }
                })}
                manualSorting
                muiToolbarAlertBannerProps={
                    isError
                        ? {
                              color: 'error',
                              children: 'Error loading data'
                          }
                        : undefined
                }
                positionToolbarAlertBanner="bottom"
                muiLinearProgressProps={({ isTopToolbar }) => ({
                    sx: {
                        display: isTopToolbar ? 'block' : 'none' //hide bottom progress bar
                    }
                })}
                renderTopToolbarCustomActions={({ table }) => (
                    <Stack direction="row" alignItems="center">
                        <Typography sx={{ fontSize: '18px', fontWeight: 500, mr: '10px' }}>Hóa Đơn</Typography>
                        {table.getSelectedRowModel().rows.length > 0 && (
                            <IconButton size="small" sx={{ mr: '5px' }} onClick={() => handleSelectRows(table.getSelectedRowModel().rows)}>
                                <Delete fontSize="small" htmlColor="black" />
                            </IconButton>
                        )}
                    </Stack>
                )}
                onColumnFiltersChange={setColumnFilters}
                onGlobalFilterChange={setGlobalFilter}
                onPaginationChange={setPagination}
                onSortingChange={setSorting}
                rowCount={rowCount}
                enableStickyHeader
                state={{
                    columnFilters,
                    globalFilter,
                    isLoading,
                    pagination,
                    showAlertBanner: isError,
                    showProgressBars: isRefetching,
                    sorting
                }}
            />
        </Box>
    );
};
