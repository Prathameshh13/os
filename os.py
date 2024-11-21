import multiprocessing
import time
import random

# Sorting function for demonstration
def bubble_sort(arr):
    n = len(arr)
    for i in range(n):
        for j in range(0, n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]

# Parent process logic
def parent_process(numbers):
    print(f"Parent process (PID: {multiprocessing.current_process().pid}) sorting numbers...")
    bubble_sort(numbers)
    print(f"Parent sorted numbers: {numbers}")
    print("Simulating zombie state: Parent will end without waiting for child.")
    time.sleep(5)
    print("Parent process ends without cleaning up child process.")

# Child process logic
def child_process(numbers):
    print(f"Child process (PID: {multiprocessing.current_process().pid}) shuffling numbers...")
    time.sleep(2)  # Simulate some processing time
    random.shuffle(numbers)
    print(f"Child shuffled numbers: {numbers}")
    print("Child process ends.")

def main():
    # Accept integers to be sorted from the user
    user_input = input("Enter integers to be sorted, separated by spaces: ")
    numbers = list(map(int, user_input.split()))

    # Create a child process
    child = multiprocessing.Process(target=child_process, args=(numbers,))
    child.start()  # Start the child process

    # Simulate parent ending before child cleanup
    parent_process(numbers)

    # Simulate zombie state: The parent process ends, but the child is still alive.
    # Uncomment the next line to properly clean up the zombie process.
    # child.join()

    # Sleep to show that the child process is briefly in a "zombie" state.
    time.sleep(10)  # Allow observation of zombie behavior.

if __name__ == "__main__":
    main()
