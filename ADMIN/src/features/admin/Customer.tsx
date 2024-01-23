import { Box, Stack, Typography } from '@mui/material';
import { child, get, ref } from 'firebase/database';
import { MaterialReactTable, type MRT_ColumnDef, type MRT_ColumnFiltersState, type MRT_PaginationState, type MRT_SortingState } from 'material-react-table';
import { useEffect, useMemo, useRef, useState } from 'react';
import { database } from '../../firebase';
import { User } from '../../models';
import SettingMenu from './Components/SettingMenu';



export const Customer = () => {
    const dbRef = ref(database);
    const [data, setData] = useState<User[]>([]);
    const [isUpdate, setIsUpdate] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);
    const [isRefetching, setIsRefetching] = useState(false);
    const [columnFilters, setColumnFilters] = useState<MRT_ColumnFiltersState>([]);
    const [globalFilter, setGlobalFilter] = useState('');
    const [sorting, setSorting] = useState<MRT_SortingState>([]);
    const [pagination, setPagination] = useState<MRT_PaginationState>({
        pageIndex: 0,
        pageSize: 10
    });
    const [open, setOpen] = useState(false);
    const settingRef = useRef<HTMLButtonElement>(null);
  
    useEffect(() => {
        const fetchData = async () => {
            if (!data.length) {
                setIsLoading(true);
            } else {
                setIsRefetching(true);
            }
            try {
                get(child(dbRef, 'customer'))
                    .then((snapshot) => {
                        if (snapshot.exists()) {
                            setData(Object.keys(snapshot.val()).map(key => snapshot.val()[key]))
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
    }, [columnFilters, globalFilter, sorting, isUpdate]);
    const columns = useMemo<MRT_ColumnDef<User>[]>(
        () => [
            { accessorKey: 'cus_name', header: 'Tên người dùng' },
            { accessorKey: 'cus_address', header: 'Địa chỉ người dùng' },
            { accessorKey: 'cus_phone', header: 'Số điện thoại' },
        ],
        []
    );
    return (
        <Box sx={{ height: '100%' }}>
            <SettingMenu anchorRef={settingRef} open={open} setOpen={setOpen} />
            <MaterialReactTable
                muiTablePaperProps={{ sx: { height: '100%' } }}
                muiTableContainerProps={{ sx: { height: 'calc(100%)' } }}
                columns={columns}
                data={data}
                // enableRowSelection
                manualFiltering
                manualPagination
                muiTableBodyRowProps={({ row }) => ({
                    
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
                        <Typography sx={{ fontSize: '18px', fontWeight: 500, mr: '10px' }}>Khách hàng</Typography>
                    </Stack>
                )}
                onColumnFiltersChange={setColumnFilters}
                onGlobalFilterChange={setGlobalFilter}
                onPaginationChange={setPagination}
                onSortingChange={setSorting}
                // rowCount={rowCount}
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
